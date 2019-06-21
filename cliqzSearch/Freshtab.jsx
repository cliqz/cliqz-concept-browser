import React from 'react';
import {
  StyleSheet,
  View,
  FlatList,
  Text,
  TouchableOpacity,
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';
import getLogo from 'cliqz-logo-database';
import { Logo } from '@cliqz/component-ui-logo';

const {
  BrowserActions,
  URLBar,
} = NativeModules;

var styles = StyleSheet.create({
  container: {
    backgroundColor: 'rgb(0, 9, 23)',
    flex: 1,
  },
  list: {
    flex: 1,
  },
  spacer: {
    // TODO: it is here to fix the problem of react-native view height which is bigger than it should
    height: 60
  },
  row: {
    padding: 10,
    flexDirection: 'row',
  },
  rowText: {
    marginLeft: 10,
    flex: 1,
    justifyContent: 'center',
  },
});

const convertLogoUrl = url => url
  .replace('logos', 'pngs')
  .replace('.svg', '_192.png');

const LogoWithText = ({ domain }) => {
  const url = `https://${domain}`;
  const logo = getLogo(url);
  const logoUrl = convertLogoUrl(logo.url);

  return (
    <TouchableOpacity
      onPress={() => BrowserActions.openLink(url, '')}
      style={styles.row}
    >
      <Logo
        logo={{
          ...logo,
          url: logoUrl,
        }}
      />
      <View style={styles.rowText}>
        <Text style={{ color: 'white' }}>{domain}</Text>
        <Text></Text>
      </View>
    </TouchableOpacity>
  );
};

const IS_SHOWN_EVENT = 'NEW_TAB:SHOW';

export default class Freshtab extends React.Component {
  state = {
    topDomains: [],
  }

  updateTopSites = () => {
    BrowserActions.topDomains(domains => {
      this.setState({
        topDomains: domains,
      });
    });
  }

  componentDidMount() {
    this.updateTopSites();
    DeviceEventEmitter.addListener(IS_SHOWN_EVENT, this.updateTopSites);
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeListener(IS_SHOWN_EVENT, this.updateTopSites);
  }

  render() {
    return (
      <View style={styles.container}>
        <FlatList
          data={this.state.topDomains}
          renderItem={({ item: domain}) => <LogoWithText key={domain} domain={domain} />}
          style={styles.list}
          inverted
        />
        <View style={styles.spacer} />
      </View>
    );
  }
}