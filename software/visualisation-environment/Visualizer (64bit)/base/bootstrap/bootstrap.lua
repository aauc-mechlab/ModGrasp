-- Provide a basic table of colours to work with
color = {
  red    = Color(1,0,0);
  green  = Color(0,1,0);
  blue   = Color(0,0,1);
  yellow = Color(1,1,0);
}
-- Recreate the dofile function
function dofile(filename)
  f = loadFile(filename);
  return f();
end