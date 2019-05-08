import { NativeModules } from 'react-native';

const Bridge = NativeModules.Bridge;

const callAction = (module, action, ...args) => Bridge.callBackgroundAction({ args, module, action });
const createModuleWrapper = (module, actions) => actions.reduce((all, action) => ({ ...all, [action]: callAction.bind(null, module, action) }), {});

export default class Cliqz {
  constructor(actions = {}) {
    this.mobileCards = createModuleWrapper('mobile-cards', [
      'openLink',
      'callNumber',
      'openMap',
      'hideKeyboard',
      'sendUIReadySignal',
      'handleAutocompletion',
      'getConfig',
      'getTrackerDetails',
    ]);
     
    this.core = createModuleWrapper('core', []);
    this.search = createModuleWrapper('search', ['getSnippet', 'reportHighlight'])
    this.geolocation = createModuleWrapper('geolocation', ['updateGeoLocation']);

    this.actions = actions;
  }
}
