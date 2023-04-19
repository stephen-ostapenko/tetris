#version 330 core

uniform int field_width;
uniform int field_height;
uniform int row;
uniform int col;

void main() {
    float cell_width = 2.0 / field_width;
    float cell_height = 2.0 / field_height;

    if (gl_VertexID == 0) {
        gl_Position = vec4(col * cell_width, row * cell_height, 0.0, 1.0);
    } else if (gl_VertexID == 1) {
        gl_Position = vec4(col * cell_width, (row + 1) * cell_height, 0.0, 1.0);
    } else if (gl_VertexID == 2) {
        gl_Position = vec4((col + 1) * cell_width, row * cell_height, 0.0, 1.0);
    } else if (gl_VertexID == 3) {
        gl_Position = vec4((col + 1) * cell_width, (row + 1) * cell_height, 0.0, 1.0);
    } else {
        // error
    }
    gl_Position -= vec4(1.0, 1.0, 0.0, 0.0);
}