import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  View,
  DeviceEventEmitter,
  NativeModules,
} from 'react-native';
import ErrorBoundry from './ErrorBoundry';
import SearchUI from 'browser-core/build/modules/mobile-cards/SearchUI';
import SearchUIVertical from 'browser-core/build/modules/mobile-cards-vertical/SearchUI';
import App from 'browser-core/build/modules/core/app';
import { Provider as CliqzProvider } from 'browser-core/build/modules/mobile-cards/cliqz';
import { Provider as ThemeProvider } from 'browser-core/build/modules/mobile-cards-vertical/withTheme';

const Bridge = NativeModules.Bridge;
class Cliqz {
  constructor(app, actions) {
    this.app = app;
    this.app.modules['ui'] = {
      status() {
        return {
          name: 'ui',
          isEnabled: true,
          loadingTime: 0,
          loadingTimeSync: 0,
          windows: [],
          state: {},
        };
      },
      name: 'ui',
      action(action, ...args) {
        return Promise.resolve().then(() => {
          return actions[action](...args);
        });
      },
      isEnabled: true,
    };
    this.mobileCards = app.modules['mobile-cards'].background.actions;
    this.geolocation = app.modules['geolocation'].background.actions;
    this.search = app.modules['search'].background.actions;
    this.core = app.modules['core'].background.actions;
  }
}

class BrowserCoreApp extends React.Component {
  state = {
    results: {},
    cliqz: null,
    config: null,
  }

  actions = {
    changeAppearance: (appearance) => {
      this.setState(prevState => ({
        results: {},
        config: {
          ...prevState.config,
          appearance,
        },
      }));
    },
  }

  onAction = ({ module, action, args, id }) => {
    return this.loadingPromise.then(() => {
      return this.state.cliqz.app.modules[module].action(action, ...args).then((response) => {
        if (typeof id !== 'undefined') {
          Bridge.replyToAction(id, response);
        }
        return response;
      });
    }).catch(e => console.error(e));
  }

  async componentWillMount() {
    const app = new App();
    let cliqz;
    this.loadingPromise = app.start().then(async () => {
      await app.ready();
      const config = await Bridge.getConfig();
      cliqz = new Cliqz(app, this.actions);
      this.setState({
        cliqz,
        config,
      });
      app.events.sub('search:results', (results) => {
        this.setState({ results })
      });
      app.events.sub('search:session-end', () => {
        this.setState({ results: {} })
      });
    });
    DeviceEventEmitter.addListener('action', this.onAction);
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeAllListeners();
  }

  reportError = error => {
    // should not happen
    if (!this.state.cliqz) {
      return;
    }

    this.state.cliqz.core.sendTelemetry({
      type: 'error',
      source: 'react-native',
      error: JSON.stringify(error),
    });
  }

  render() {
    if (!this.state.config || !this.state.cliqz) {
      return null;
    }
    const results = this.state.results.results || [];
    const meta = this.state.results.meta || {};
    const appearance = this.state.config.appearance
    const SearchComponent = this.state.config.layout === "horizontal" ? SearchUI : SearchUIVertical;
    return (
      <ErrorBoundry results={results} reportError={this.reportError}>
        <View style={styles.container}>
          <CliqzProvider value={this.state.cliqz}>
            <ThemeProvider value={appearance}>
              <SearchComponent results={results} meta={meta} theme={appearance} />
            </ThemeProvider>
          </CliqzProvider>
        </View>
      </ErrorBoundry>
    );
  }
}

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
});

AppRegistry.registerComponent('BrowserCoreApp', () => BrowserCoreApp);
