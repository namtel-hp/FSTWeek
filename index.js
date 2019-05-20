
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNewMeetUpPostNotification = functions.database.ref('/Chats/conf/{pushId}')
    .onCreate(async (change, context) => {

      const getDeviceTokensPromise = admin.database()
          .ref('/UserTokens').once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'New Disscusiion',
          body: 'You have a new post in conference meet up discccusion. Tap to see.'
        
        }
      };

      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
exports.sendNewMeetUpScheduleNotification = functions.database.ref('/Meetup/conf/')
    .onCreate(async (change, context) => {


      const getDeviceTokensPromise = admin.database()
          .ref('/UserTokens').once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];
  
      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'A meet up is scheduled',
          body: 'A meet up program is scheduled for all participating people. Tap to see!'
        
        }
      };

      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });

exports.sendNewMeetUpScheduleChangeNotification = functions.database.ref('/Meetup/conf/')
    .onUpdate(async (change, context) => {


      // If un-follow we exit the function.


      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref('/UserTokens').once('value');

      
   

      // The snapshot to the user's tokens.
      let tokensSnapshot;

      // The array containing all the user's tokens.
      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];
  

      // Check if there are any device tokens.
      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');


      // Notification details.
      const payload = {
        notification: {
          title: 'Meet up schedule is changed!',
          body: 'Tap to see.'
        
        }
      };

      // Listing all tokens as an array.
      tokens = Object.keys(tokensSnapshot.val());
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokens, payload);
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });


exports.sendPanelMsgNotification = functions.database.ref('/PanelDisc/accepted/')
    .onCreate(async (change, context) => {


      // If un-follow we exit the function.


      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref('/UserTokens').once('value');


   

      // The snapshot to the user's tokens.
      let tokensSnapshot;

      // The array containing all the user's tokens.
      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];
  

      // Check if there are any device tokens.
      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');


      // Notification details.
      const payload = {
        notification: {
          title: 'New post',
          body: 'You have new post in panel disscussions. Tap to see.'
        
        }
      };

      // Listing all tokens as an array.
      tokens = Object.keys(tokensSnapshot.val());
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokens, payload);
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });


exports.sendMentorChatNotification = functions.database.ref('/Chats/mentDirect/{group}/{pushId}')
    .onCreate(async (change, context) => {
      const group = context.params.group;
  
      const getDeviceTokensPromise = admin.database().ref(`/MentUserTokens/${group}`).once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'New message',
          body: 'You have a new message in your mentor group chat. Tap to see.'
        }
      };


      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });


exports.sendMentorMeetUpNotification = functions.database.ref('/MentorshipGroups/{group}/meetup')
    .onCreate(async (change, context) => {
      const group = context.params.group;
  
      const getDeviceTokensPromise = admin.database().ref(`/MentUserTokens/${group}`).once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'A meet up is scheduled',
          body: 'For your mentor group, a meet up program is scheduled. Tap to see!'
        }
      };


      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });

exports.sendMentorMeetUpChangeNotification = functions.database.ref('/MentorshipGroups/{group}/meetup')
    .onUpdate(async (change, context) => {
      const group = context.params.group;
  
      const getDeviceTokensPromise = admin.database().ref(`/MentUserTokens/${group}`).once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'Change in meet up schedule',
          body: 'The meet up program is changed for your metor meet up program. Tap to see!'
        }
      };


      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });

exports.sendPanelApproveMsgNotification = functions.database.ref('/PanelDisc/pending/')
    .onCreate(async (change, context) => {


      // If un-follow we exit the function.


      // Get the list of device notification tokens.
      const getDeviceTokensPromise = admin.database()
          .ref('/AdminUserTokens').once('value');


   

      // The snapshot to the user's tokens.
      let tokensSnapshot;

      // The array containing all the user's tokens.
      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];
  

      // Check if there are any device tokens.
      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');


      // Notification details.
      const payload = {
        notification: {
          title: 'New Post To Review',
          body: 'You have a new post to review in panel discussions. Tap to see.'
        
        }
      };

      // Listing all tokens as an array.
      tokens = Object.keys(tokensSnapshot.val());
      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokens, payload);
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });

exports.sendNewMentorGroupNotification = functions.database.ref('/MentorshipGroups/{group}')
    .onUpdate(async (change, context) => {
      const group = context.params.group;
  
      const getDeviceTokensPromise = admin.database().ref('/AdminUserTokens').once('value');


      let tokensSnapshot;

      let tokens;

      const results = await Promise.all([getDeviceTokensPromise]);
      tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
        return console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      const payload = {
        notification: {
          title: 'New Mentor Group Formed',
          body: 'A new mentor group a formed, add an event for their meet up program. Tap to see!'
        }
      };


      tokens = Object.keys(tokensSnapshot.val());

      const response = await admin.messaging().sendToDevice(tokens, payload);

      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
