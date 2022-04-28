import { registerPlugin } from '@capacitor/core';

export interface ZoomAndroidPlugin {

  tryJoinMeeting(
    options: object
  ): Promise<{ value: object }>;

}

export default registerPlugin<ZoomAndroidPlugin>('ZoomAndroid');
