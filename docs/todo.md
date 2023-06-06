# General TODO & Development plan

## User profiles

Add the ability to view and edit user profiles

- [x] Profile photo
- [x] Registration date
- [x] Description/info field
- [x] Profile view page:
    * General information
    * List of uploaded files with search and sorting
- [x] Profile edit page
- [ ] Email verification?

## Audio

Improve audio upload and playback

- [x] Audio format recognition and support on upload
- [x] Audio search
- [x] Audio sorting
- [ ] Custom playback element with controls
- [x] Cover image upload
- [x] Proper audio deletion
- [x] Audio view page
- [x] Editing page
- [x] Reaction button
- [x] Comments
- [ ] Playlists

## Front-end general

General front-end improvements and goals

- [ ] Substitute Bootstrap with own styles
- [ ] Improve page layout
- [x] AJAX content loading
- [ ] Add localization support
- [x] Add error page
- [ ] Hide elements that require authentication
- [ ] Show errors during form validation

## Back-end general

General back-end improvements and goals

- [x] Model and view model separation
- [x] Improve data validation
- [ ] Cover code with tests
- [x] Storage service for user-uploaded content
- [x] Content retrieval API:
    * Audio with search, pagination and sorting
    * Images (audio covers, user profile pictures)
- [x] Image compression and resizing
- [ ] Optimize image resizing
