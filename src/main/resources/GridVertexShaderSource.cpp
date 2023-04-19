#version 330 core

const vec2 VERTICES[3] = vec2[3](
    vec2(0.0, 1.0),
    vec2(-sqrt(0.75), -0.5),
    vec2( sqrt(0.75), -0.5)
);

const vec3 COLORS[3] = vec3[3](
    vec3(1.0, 0.0, 0.0),
    vec3(0.0, 1.0, 0.0),
    vec3(0.0, 0.0, 1.0)
);

out vec3 color;

void main() {
    vec2 position = VERTICES[gl_VertexID];
    gl_Position = vec4(position, 0.0, 1.0);
    color = COLORS[gl_VertexID];
}