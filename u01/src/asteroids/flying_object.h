#include <ml5/ml5.h>
#pragma once
class flying_object {
public:

	using context_t = ml5::paint_event::context_t;

	flying_object(wxPoint pos) : position{ pos } {}

	void set_position(wxPoint pos) {
		this->position = pos;
	}

	wxPoint get_position() {
		return this->position;
	}

	void draw(context_t & ctx) {
		ctx.SetBrush(*wxWHITE_BRUSH);
		ctx.SetPen(*wxBLACK_PEN);
		ctx.DrawRectangle(wxRect{ this->position, wxSize{10, 10} });
	}


private:
	wxPoint position;
};