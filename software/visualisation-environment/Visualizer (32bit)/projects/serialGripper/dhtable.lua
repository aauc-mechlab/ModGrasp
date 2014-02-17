local fingerA = DHTable {
  DHRotation(0,0,1.5,0),
  DHRotation(5.9,PI/2,0,0),
  DHRotation(3.2,0,0,0),
  DHRotation(3.2,0,0,0)
}

local fingerB = DHTable {
  DHRotation(3.2,0,0,0),
  DHRotation(3.2,0,0,0),
  DHRotation(3.2,0,0,0),
}

local fingerC = DHTable {
  DHRotation(0,0,1.5,0),
  DHRotation(5.9,PI/2,0,0),
  DHRotation(3.2,0,0,0),
  DHRotation(3.2,0,0,0)
}

local positionA = matrix.rotation(0,0,0):mul(matrix.translation(3.2,0,-1.7));
local positionB = matrix.rotation(0,PI/2,PI/2):mul(matrix.translation(0,-0.3,0));
local positionC = matrix.rotation(0,0,PI):mul(matrix.translation(-3.2,0,-1.7));
fingerA:transform(positionA);
fingerB:transform(positionB);
fingerC:transform(positionC);

return fingerA, fingerB, fingerC