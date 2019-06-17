import React from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button,
  NativeModules,
} from 'react-native';

const BrowserActions = NativeModules.BrowserActions;

var styles = StyleSheet.create({
  container: {
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
      </View>
    )
  }
}