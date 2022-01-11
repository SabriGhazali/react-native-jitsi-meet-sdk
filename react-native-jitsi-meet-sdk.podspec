require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-jitsi-meet-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "12.0" }
  s.source       = { :git => "https://github.com/SabriGhazali/react-native-jitsi-meet-sdk.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"

  s.dependency "React-Core"

  s.subspec 'ConferenceController' do |cc|
    cc.name             = "ConferenceController"
    cc.source_files     = "ios/Conference/Conference/*.{h,m}"
    cc.exclude_files    = "ios/Conference/Conference/Conference.h"
    cc.resource_bundles = { "Conference" => "ios/Conference/Conference/*.{lproj,storyboard}" }
    cc.requires_arc     = true
    cc.dependency 'JitsiMeetSDK', '4.1.0'
  end

end
