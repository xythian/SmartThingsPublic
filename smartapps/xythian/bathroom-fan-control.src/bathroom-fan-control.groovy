/**
 *  Bathroom Fan Control
 *
 *  Copyright 2015 Ken Fox
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Bathroom Fan Control",
    namespace: "xythian",
    author: "Ken Fox",
    description: "Automatically control bathroom fan with a timer.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

def switchChange(evt) {
	log.debug "SwitchChange: $evt.name: $evt.value"
    
    if(evt.value == "on") {
        runIn(fanDuration, laterTrigger, [overwrite: true])
        // sendNotification("start fan timer")        
        state.status = "on"
    } else {
    	state.status = "off"
    }
}

def laterTrigger() {
    if ( state.status == "on" ) {
        state.status = "off"
        fanSwitch.off()
        // sendNotification("turned fan off")
    }
}

preferences {
	section("Turn on/off fan..."){
		input "fanSwitch", "capability.switch", multiple: false, title: "Select Switch"
        input "fanDuration", "number", title: "Time in seconds", required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(fanSwitch, "switch", switchChange)
    state.status = "off"
    log.debug "Status: ${state.status}"
}
