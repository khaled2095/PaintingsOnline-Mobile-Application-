//
//  DLARSessionState.swift
//  ARKitPlanesAndObjects
//
//  Created by Ignacio Nieto Carvajal on 13/11/2017.
//  Copyright © 2017 Digital Leaves. All rights reserved.
//

import Foundation

enum ARCoffeeSessionState: String, CustomStringConvertible {
    case initialized = "initialized"
    case ready = "ready"
    case temporarilyUnavailable = "temporarily unavailable"
    case failed = "failed"

    var description: String {
        switch self {
        case .initialized:
            return "Search for a flat surface"
        case .ready:
            return "click to place the paintings"
        case .temporarilyUnavailable:
            return "something is wrong, please try again"
        case .failed:
            return "Error, restart the app"
        }
    }
}
