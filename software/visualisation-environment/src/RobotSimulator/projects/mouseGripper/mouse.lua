local handler = {}

function handler.onMove(x,y,dx,dy)
  synB = ((x/width))*1024
  synA = 1024-(((y/height))*1024)
end

function handler.onScroll(x,y,ticks)
    local dir = ticks/math.abs(ticks);
    camPos = camPos - (camPos:normalize()*(dir));
    cam:position(camPos);
end
return handler;