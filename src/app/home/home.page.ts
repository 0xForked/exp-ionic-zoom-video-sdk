import { Component } from '@angular/core';
import zoomAndroid from '../../plugins/zoom-android.plugin';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {}

  // ! ADD TOKEN
  // ? GET TOKEN FROM ZOOM WEB SAMPLE
  async performEnterConsultationRoom() {
    zoomAndroid.tryJoinMeeting({
      // eslint-disable-next-line max-len
      appointmentToken: '',
      appointmentSessionName:'tesName123',
      customerFullName: 'Lorem Soparto'
    });
  }
}
