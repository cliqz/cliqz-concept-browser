import React from 'react';
import {
  StyleSheet,
  View,
  Image,
  TouchableHighlight,
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
        <TouchableHighlight onPress={() => URLBar.fillIn('')}>
          <Image
            source={require('./ic_launcher_round.png')}
            style={{ alignSelf: "center" }}
            on
            />
        </TouchableHighlight>
      </View>
    )
  }
}