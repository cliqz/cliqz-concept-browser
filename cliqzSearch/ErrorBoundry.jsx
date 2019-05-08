import React from 'react';

export default class extends React.Component {
  state = {
    hasError: false,
  }

  componentDidCatch(error) {
    this.props.reportError(error);
    this.setState({
      hasError: true,
    });
    return;
  }

  componentWillReceiveProps() {
    this.setState({
      hasError: false
    });
  }

  render() {
    return this.state.hasError
      ? null
      : this.props.children;
  }
}