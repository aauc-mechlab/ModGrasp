#version 330 core
uniform vec3 lightAcolor;
uniform vec3 lightAdir;
out vec3 fragCol;
in vec3 norm;
in vec3 col;

void main(){
  float cosTheta = dot( normalize(norm),lightAdir );
  fragCol = cosTheta*col*lightAcolor;
}