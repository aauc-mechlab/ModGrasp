--- Camera movement flags
closer=false; further=false; 
left=false; right=false;
up=false; down=false;


-- Serial communication input
function onSerialText(text)
  synA,synB=text:match("(%d+.*%d*):::(%d+.*%d*)");
end

-- Mouse and keyboard events
  keyboardhandler = dofile("keyboard.lua");
  communicationObject = serial.StringReader("COM7",9600,onSerialText);
--mousehandler = dofile("mouse.lua");

-- Load fingers with an empty input vector
fingerA,fingerB,fingerC = dofile("dhtable.lua");
input = Vector(0,0,0,0,0,0,0,0,0,0,0);

-- Load models
models = dofile("models.lua");
synA=0;
synB=0;


S = dofile("synergies.lua");
function buildSynergies()
  local synA=1-(tonumber(synA)/1023);
  local synB=1-(tonumber(synB)/1023);
  local act = Vector(2*synA+0.15, synB);
  input = S:transform(act);
end







function init()
  box = Model(face.box(Vertex(-1,-1,-1),Vertex(1,1,1),color.red));
  box:init();
  models:init();
  cam:position(camPos);
end

-- Release models here
function destroy()
  box:destroy();
  models:destroy();
  communicationObject:close();
end


transBaseOrientation = matrix.rotation(PI/2,PI,0);

-- Position and render models
function render()
  buildSynergies();
  cameraController();
  b,a = input:split(3);
  a,c = a:split(4);
  fingerA:apply(a); fingerB:apply(b); fingerC:apply(c);
  
  drawBase();
  drawRotatingFinger(fingerA,servoBaseA);
  drawRotatingFinger(fingerC,servoBaseB);
  drawThumb(fingerB);
end

function drawBase()
  models.base:render(transBaseOrientation);
  models.baseMount:render(transBaseOrientation);
end


fingerEnd = matrix.rotation(0,PI/2,0):mul(matrix.translation(4.4,-0.85,0));
sBracket = matrix.rotation(-PI/2,0,PI/2):mul(matrix.translation(3.2,0,0));
lBracket = matrix.rotation(-PI/2,0,PI/2):mul(matrix.translation(4.6,0,0));
mBracket = matrix.rotation(PI,-PI/2,0):mul(matrix.translation(0,-1.7,0));
servo    = matrix.rotation(-PI/2,0, PI/2):mul(matrix.translation(0,0,0));
servoBaseA   = matrix.rotation(-PI/2,PI/2, -PI/2):mul(matrix.translation(0,0,0.3));
servoBaseB   = matrix.rotation(-PI/2,-PI/2, -PI/2):mul(matrix.translation(0,0,0.3));
fingerBaseA = matrix.rotation(PI/2,0,-PI/2):mul(matrix.translation(0,0,-1.5));
fingerBaseB = matrix.rotation(0,0, PI/2):mul(matrix.translation(4.4+1.5,0,0));
fingerBaseC = matrix.rotation(0,PI/2, 0):mul(matrix.translation(0,0,0));

function drawRotatingFinger(finger,motorBase)
  models.servo:render(motorBase:mul(finger:transform()));
  models.largeBracket:render(fingerBaseA:mul(finger:transform(0)));
  models.smallBracket:render(fingerBaseB:mul(finger:transform(0)));
  models.smallBracket:render(sBracket:mul(finger:transform(1)));
  models.smallBracket:render(sBracket:mul(finger:transform(2)));
  models.servo:render(fingerBaseC:mul(finger:transform(1)));
  models.servo:render(fingerBaseC:mul(finger:transform(2)));
  models.servo:render(fingerBaseC:mul(finger:transform(3)));
  models.motorBracket:render(mBracket:mul(finger:transform(1)));
  models.motorBracket:render(mBracket:mul(finger:transform(2)));
  models.motorBracket:render(mBracket:mul(finger:transform(3)));
  models.largeBracket:render(lBracket:mul(finger:transform(3)));
  models.smallBracket:render(fingerEnd:mul(finger:transform(3)));
end



TServo = matrix.rotation(0,PI/2,0):mul(matrix.translation(0,0,0));
TMBracket = matrix.rotation(0,-PI/2,PI):mul(matrix.translation(0,-1.7,0));
TSBracket = matrix.rotation(PI/2,0,PI/2):mul(matrix.translation(3.2,0,0));
TFingerEnd = matrix.rotation(0,PI/2,0):mul(matrix.translation(3.2,-0.85,0));

function drawThumb(finger)
  -- Render servos
  models.servo:render(TServo:mul(fingerB:transform(0)));
  models.servo:render(TServo:mul(fingerB:transform(1)));
  models.servo:render(TServo:mul(fingerB:transform(2)));
  -- Render servo brackets
  models.motorBracket:render(TMBracket:mul(fingerB:transform(0)));
  models.motorBracket:render(TMBracket:mul(fingerB:transform(1)));
  models.motorBracket:render(TMBracket:mul(fingerB:transform(2)));
  -- Render small brackets
  models.smallBracket:render(TSBracket:mul(fingerB:transform(0)));
  models.smallBracket:render(TSBracket:mul(fingerB:transform(1)));
  models.smallBracket:render(TSBracket:mul(fingerB:transform(2)));
  -- Render thumbpad
  models.smallBracket:render(TFingerEnd:mul(fingerB:transform(2)));

end






camPos = Vector(20,20,20);
camRotateR = matrix.rotation(0,0,0.001);
camRotateL = matrix.rotation(0,0,-0.001);

function cameraController()
  if (left and not right) then
    camPos=camRotateL:transform(camPos);
    cam:position(camPos);
  end
  if (right and not left) then
    camPos=camRotateR:transform(camPos);
    cam:position(camPos);
  end
  if (closer and not further) then
    camPos = camPos-(camPos:normalize()*0.05);
    cam:position(camPos);
  end
  if (further and not closer) then
    camPos = camPos+(camPos:normalize()*0.05);
    cam:position(camPos);
  end
  if (up and not down) then
    camPos = camPos+(Vector(0,0,0.02));
    cam:position(camPos);
  end
  if (down and not up) then
    camPos = camPos-(Vector(0,0,0.02));
    cam:position(camPos);
  end
end