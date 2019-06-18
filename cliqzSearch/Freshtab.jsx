import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button,
  NativeModules,
} from 'react-native';

const {
  BrowserActions,
  URLBar,
} = NativeModules;

var styles = StyleSheet.create({
  container: {
    backgroundColor: 'rgb(0, 9, 23)',
    flex: 1,
    justifyContent: 'center',
  },
});

export default class Freshtab extends React.Component {

  render() {
    return (
      <View style={styles.container}>
        <Text>This is FreshTab</Text>
        <Button
          onPress={() => { BrowserActions.openLink('https://cliqz.com', '')}}
          title="This is a freshtab link"
        />
        <Button
          title="Start Search"
          onPress={() => { URLBar.fillIn('') }}
        />
      </View>
    )
  }
}