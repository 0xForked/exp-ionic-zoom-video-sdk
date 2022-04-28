import { Component } from '@angular/core';
import zoomAndroid from '../../plugins/zoom-android.plugin';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {}

  async performEnterConsultationRoom() {
    zoomAndroid.tryJoinMeeting({
      appointmentToken: 'qwe',
      appointmentSessionName:'zxc',
      customerFullName: 'Lorem Soparto'
    });
  }
}
