Requirements
    3 parts
        Editor
            Allows users to edit control points on the warp lattice
                Provides feedback in the form of an overlay of the warped image
                with the target image
            Allows users to submit their work to the server
                Provides feedback upon successful submission
        Server
            Receives requests from the editor for a job
                Returns the default lattice, links to the warp and target
                images, and a job ID
                Marks the database with an open job with the given ID, warp image,
                target image, current (start) time
            Receives data from the editor
                Stores warp data and current (finish) time in database with the
                particular ID
        Viewer
            Takes an image and a warp and outputs a new image based on that warp

Specification
    Warp
        Combination of affine transformation and translated control points.
        All control points are in terms of image space [0, 1]x[0, 1] with
        (0, 0) in the **bottom left corner**.
        Warp = {Affine, ControlPoints}
        Affine = {(dx1, dy1), (dx2, dy2)}
            {(1, 0), (0, 1)} -> identify affine transform
        ControlPoints = Src, Dst, Trianglulation
        Src = {(x1, y1), (x2, y2), ...}
        Dst = {(x1, y1), (x2, y2), ...}
            Src == Dst -> identity control point warp
        Triangulation = {(p1, p2, p3), ...}
            Index buffer into Src and Dst
    Editor
        Inputs:
            Warp image
            Target image
            Mesh
                Dst and Triangulation (Src == Dst, and Affine = default)
        States:
            Edit Mode
                Affine tool
                    Allows editing 2 points
                    Restrict to translation, rotation, and scaling (no shear)
                    Draw box
                Mesh tool
                    Draw mesh
                        unselected points in blue
                        mesh boundaries in light grey
                        selected point in green
                        selected point being pulled too far is red
                    On mouse down:
                        Select nearest point
                            Implement by looping through all points
                            Transform to screen space, then compare distances
                        Selected point is green
                    On mouse move:
                        If selected point wouldn't cross triangulation boundary
                        to reach new mouse position, move selected point
                        Else don't move selected point and color it red
                    On mouse up:
                        Unselect previously-selected point
            Submit Mode
                Display a progress bar showing upload status
                Make http requests to MTurk API and server
Design
    class Warp
    class WarpRenderer
        Renders a warped image on top of a target image
    interface Tool extends GLEventListener, MouseListener
    class MeshTool implements Tool
        Renders an overlay of the mesh with colored points and lines
        Handles mouse input events to properly deal with 
    class AffineTool implements Tool
        Renders an overlay of an affine transformation representation
    class Editor extends Applet
        Toggles between MeshTool and AffineTool
        Renders with WarpRenderer and overlays the current tool
        Passes mouse events to the current tool
    class Viewer
        Standalone command-line utility to warp images