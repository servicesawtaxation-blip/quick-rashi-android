# QUICK RASHI V 14.2.5 — Embedded Android WebView

Opens the assigned Google Apps Script URL in a dedicated app interface. No Chrome installation required for ordinary public web app access. Android Autofill may suggest saved passwords if an autofill provider supports WebView; this app never stores passwords itself.

**Limitation:** Google's OAuth login is unsupported within embedded WebViews. If Google login is required, the app opens an external secure browser; Chrome is not required but a compatible browser is. Camera permission applies only to the hosted scanner origin. The Apps Script iframe can still restrict camera capture; its hosted scanner is preferred.

Upload repo files to GitHub, then Actions > Build QUICK RASHI APK > Artifacts. Test login, camera, downloads, device Back, financial reports on your actual phone before production rollout.
