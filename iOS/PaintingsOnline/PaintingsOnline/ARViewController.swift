//
//  ARViewController.swift
//  PaintingsOnline
//
//  Created by Mohammed Alharthy on 18/9/19.
//  Copyright © 2019 Mohammed Alharthy. All rights reserved.
//

import UIKit
import ARKit

class ARViewController:  UIViewController, ARSCNViewDelegate , ARSessionDelegate{

      @IBOutlet weak var statusLabel: UILabel!
      
    @IBOutlet weak var sceneView: ARSCNView!
    // Planes: every plane is identified by a UUID.
      var planes = [UUID: VirtualPlane]() {
          didSet {
              if planes.count > 0 {
                  currentCaffeineStatus = .ready
              } else {
                  if currentCaffeineStatus == .ready { currentCaffeineStatus = .initialized }
              }
          }
      }
      var currentCaffeineStatus = ARCoffeeSessionState.initialized {
          didSet {
              DispatchQueue.main.async { self.statusLabel.text = self.currentCaffeineStatus.description }
          }
      }
      var selectedPlane: VirtualPlane?
      var mugNode: SCNNode!
      
      override func viewDidLoad() {
          super.viewDidLoad()
          
          // Set the view's delegate
          sceneView.delegate = self
          
          // Show statistics such as fps and timing information
          sceneView.showsStatistics = true
          
          // configure settings and debug options for scene
          self.sceneView.debugOptions = [ARSCNDebugOptions.showFeaturePoints, SCNDebugOptions.showConstraints, SCNDebugOptions.showLightExtents]
          self.sceneView.automaticallyUpdatesLighting = true

          // Create a new scene
          let scene = SCNScene()
          
          // Set the scene to the view
          sceneView.scene = scene
          
          // round corners of status label
          statusLabel.layer.cornerRadius = 20.0
          statusLabel.layer.masksToBounds = true

      }
      
    
    // Inserting 3D Geometry for ARHitTestResult
    func insertGeometry(for result: ARHitTestResult) {
     
     // Method 2: Add SCNNode at position
     let position = SCNVector3(
       result.worldTransform.columns.3.x,
       result.worldTransform.columns.3.y,
       result.worldTransform.columns.3.z
     )
     Painting.position = position
    }

    // Intercept a touch on screen and hit-test against a plane surface
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if (touches.count == 1 ) {
     guard let touch = touches.first else { return }
     let point = touch.location(in: sceneView)
     
     let result = sceneView.hitTest(point, types: .existingPlaneUsingExtent)
     guard result.count > 0 else {
       print("No plane surfaces found")
       return
     }
     
     insertGeometry(for: result[0])
        }
    }
    
    var Painting: SCNNode!
     var currentAngleY: Float = 0.0
   @objc func didRotate(_ gesture: UIRotationGestureRecognizer) {
          guard let _ = Painting else { return }
          // Negative for correct direction
          var newAngleY = (Float)(-gesture.rotation)
          
          newAngleY += currentAngleY
          Painting?.eulerAngles.y = newAngleY
          
          if gesture.state == .ended{
              currentAngleY = newAngleY
          }
      }
    
      override func viewWillAppear(_ animated: Bool) {
          super.viewWillAppear(animated)
          //Store The Rotation Of The CurrentNode
        
          let rotateGesture = UIRotationGestureRecognizer(target: self, action: #selector(didRotate(_:)))
                sceneView.addGestureRecognizer(rotateGesture)
          // Create a session configuration
          let configuration = ARWorldTrackingConfiguration()
          configuration.planeDetection = .vertical
          
          // Run the view's session
          sceneView.session.run(configuration)
          if planes.count > 0 { self.currentCaffeineStatus = .ready }
      }
      
      override func viewWillDisappear(_ animated: Bool) {
          super.viewWillDisappear(animated)
          
          // Pause the view's session
          sceneView.session.pause()
          self.currentCaffeineStatus = .temporarilyUnavailable
      }
      
      
      // MARK: - Adding, updating and removing planes in the scene in response to ARKit plane detection.
      
      func renderer(_ renderer: SCNSceneRenderer, didAdd node: SCNNode, for anchor: ARAnchor) {
          // create a 3d plane from the anchor
          if let arPlaneAnchor = anchor as? ARPlaneAnchor {
              let plane = VirtualPlane(anchor: arPlaneAnchor)
              self.planes[arPlaneAnchor.identifier] = plane
              node.addChildNode(plane)
              print("Plane added: \(plane)")
            if (Painting == nil) {
            let boxGeometry = SCNBox(width: 0.8, height: 0.5, length: 0.01, chamferRadius: 0)
                Painting = SCNNode(geometry: boxGeometry)
                
                
                
                let material = SCNMaterial()
                material.diffuse.contents = UserDefaults.standard.imageForKey(key:"SavedImage")
                
                boxGeometry.materials = [material]
                // Method 1: Add Anchor to the scene
          sceneView.scene.rootNode.addChildNode(Painting)
          }
        }
      }
      
      func renderer(_ renderer: SCNSceneRenderer, didUpdate node: SCNNode, for anchor: ARAnchor) {
          if let arPlaneAnchor = anchor as? ARPlaneAnchor, let plane = planes[arPlaneAnchor.identifier] {
              plane.updateWithNewAnchor(arPlaneAnchor)
              print("Plane updated: \(plane)")
          }
      }
      
      func renderer(_ renderer: SCNSceneRenderer, didRemove node: SCNNode, for anchor: ARAnchor) {
          if let arPlaneAnchor = anchor as? ARPlaneAnchor, let index = planes.index(forKey: arPlaneAnchor.identifier) {
              print("Plane updated: \(planes[index])")
              planes.remove(at: index)
          }
      }
      

      func virtualPlaneProperlySet(touchPoint: CGPoint) -> VirtualPlane? {
          let hits = sceneView.hitTest(touchPoint, types: .existingPlaneUsingExtent)
          if hits.count > 0, let firstHit = hits.first, let identifier = firstHit.anchor?.identifier, let plane = planes[identifier] {
              self.selectedPlane = plane
              return plane
          }
          return nil
      }
      
}
