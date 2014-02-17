local models = {};
function models:init()
  self.base:init();
  self.baseMount:init();
  self.largeBracket:init();
  self.smallBracket:init();
  self.motorBracket:init();
  self.servo:init();
end
function models:destroy()
  self.base:destroy();
  self.baseMount:destroy();
  self.largeBracket:destroy();
  self.smallBracket:destroy();
  self.motorBracket:destroy();
  self.servo:init();
end

models.base = Model(
  face.fromFile("models/base.obj",Color(1,0.7,0.3))
);

models.baseMount = Model(
  face.fromFile("models/baseMount.obj",Color(0.7,0.7,0.7))
);

models.largeBracket = Model(
  face.fromFile("models/largeBracket.obj",color.green)
);

models.smallBracket = Model(
  face.fromFile("models/smallBracket.obj",color.green)
);

models.motorBracket = Model(
  face.fromFile("models/motorBracket.obj",color.yellow)
);

models.servo = Model(
  face.fromFile("models/servo.obj",color.red)
);


return models;