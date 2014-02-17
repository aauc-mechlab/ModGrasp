#version 330 core
layout(location = 0)in vec3 vert;
layout(location = 1)in vec3 normal;
layout(location = 2)in vec3 colours;
uniform mat4 Model = mat4(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
uniform mat4 ViewProjection = mat4(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
out vec3 norm;
out vec3 col;
void main()
{
    vec4 v = ViewProjection*Model*vec4(vert,1);
    
    
    col=colours;
    norm=normalize((transpose(inverse(Model))*vec4(normal,1)).xyz);
    gl_Position = v;
}
