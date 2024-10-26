import SwiftUI

@main
struct iOSApp: App {
  init() {
        // Initialize Koin when the app starts
        KoinModuleKt.initKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}