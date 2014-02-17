============================================
  Overview and usage tutorial for simulator
============================================

The simulator requires only one file in a project, which is main.lua
For the script to be of any use to the simulator, it needs to have three functions defined:

function init()           | Called when the scene is to be set up.
end                       | This is where models should be initialized

function destroy()        | Called before program shutdown
end                       | This is where models should have their destroy-methods called

function render()         | Called once every render-cycle
end                       | This is where the actual rendering of models should take place



To aid in the representation of the physical robot or machine, a handful of classes have been
exposed in the Lua-environment as userdata, and have creation-functions and metatables set accordingly.
These will be shown in use as examples
Vector:
  a = Vector(1,0,0)
  b = Vector(1,0,1)
  c = a + b
  d = a - b
  e = a:dot(b)
  f = a:cross(b)
  g = c:normalize();


Matrix:
  a = Matrix{
    {1,2},
    {3,4}
  }
  b = Matrix{
    {0,1},
    {1,0}
  }
  c = a+b
  d = a-b
  e = a:mul(b)
  v = b:transform(Vector(1,0))

DHTables:
  construct = DHTable {
    DHRotation(a,alpha,d,theta),
    DHRotation(0,PI/2,3,0),
    DHPrismatic(0,0,0,0)
  }
  construct:transform(Matrix{{0.5,0},{0,0.5}});
  a = construct:transform(0) <-- Retrieve the individual transformation matrixes for the joints
  b = construct:transform(1)
  c = construct:transform(2)

Beyond this, the examples provided in the visualization of the three-fingered gripper should be adequate for seeing how models are loaded and rendered.


When exporting models from blender for use with the visualizer, remember to export normals, make all surfaces triangles, and set coordinate-axes to Z-up