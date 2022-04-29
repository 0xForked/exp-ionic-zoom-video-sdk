import { Component } from '@angular/core';
import zoomAndroid from '../../plugins/zoom-android.plugin';
import * as moment from 'moment';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  enableCamera = true;
  enableMicrophone = true;

  constructor() {}


  // ! ADD TOKEN
  // ? GET TOKEN FROM ZOOM WEB SAMPLE
  async performEnterConsultationRoom() {
    const now = moment();
    const appointmentSessionStartAt = now.unix();
    const appointmentSessionEndAt = now.add(10, 'minutes').unix();

    zoomAndroid.tryJoinMeeting({
      // eslint-disable-next-line max-len
      appointmentToken: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBfa2V5IjoiS2p5aVgwTWEyM0JOb0pCMjZZUXB4Z1ZVSG5qcjNTRmlJc09rIiwiaWF0IjoxNjUxMjE4Nzk0LCJleHAiOjE2NTEyMjU5OTQsInRwYyI6InRlc05hbWUxMjMiLCJwd2QiOiIiLCJ1c2VyX2lkZW50aXR5IjoiIiwic2Vzc2lvbl9rZXkiOiIiLCJyb2xlX3R5cGUiOjF9.JDIvEqjAGyGCctl6BkIjBKVzoJFAHji5SLBcLqZIvWQ',
      appointmentSessionName:'tesName123',
      customerFullName: 'Lorem Soparto',
      enableCamera: this.enableCamera,
      enableMicrophone: this.enableMicrophone,
      appointmentSessionStartAt,
      appointmentSessionEndAt
    });
  }
}
