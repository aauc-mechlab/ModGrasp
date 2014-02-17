--     Keyboard
--
--       WASD
--      *----*
--      | 17 |
-- *----*----*----*
-- | 30 | 31 | 32 |
-- *----*----*----*

local handler = {}
function handler.onDown(key,char)
  if (key==30) then left  = true; end
  if (key==31) then further  = true; end
  if (key==32) then right = true; end
  if (key==17) then closer    = true; end
  if (key==1) then exit(); end
  if (key==16) then up=true; end
  if (key==44) then down=true; end
end

function handler.onUp(key,char)
  if (key==30) then left  = false; end
  if (key==31) then further  = false; end
  if (key==32) then right = false; end
  if (key==17) then closer    = false; end
  if (key==16) then up=false; end
  if (key==44) then down=false; end
end

return handler;