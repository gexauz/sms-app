import React, { useEffect } from 'react';
import { View, Text, PermissionsAndroid } from 'react-native';

export default function App() {
  useEffect(() => { requestPermissions(); }, []);

  const requestPermissions = async () => {
    await PermissionsAndroid.requestMultiple([
      PermissionsAndroid.PERMISSIONS.RECEIVE_SMS,
      PermissionsAndroid.PERMISSIONS.READ_SMS,
    ]);
  };

  return (
    <View style={{ flex:1, justifyContent:'center', alignItems:'center' }}>
      <Text>SMS Background Sync Active</Text>
    </View>
  );
}