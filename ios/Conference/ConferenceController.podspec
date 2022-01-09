Pod::Spec.new do |s|
  s.name             = "ConferenceController"
  s.version          = "1.0.0"
  s.license          = "MIT"
  s.source_files     = "Conference/*.{h,m}"
  s.exclude_files    = "Conference/Conference.h"
  s.resource_bundles = { "Conference" => "Conference/*.{lproj,storyboard}" }
  s.platform         = :ios, "12.0"
  s.requires_arc     = true
end
