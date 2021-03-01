#include <ml5/ml5.h>
#pragma once

enum class rotate_direction {
	right, left
};

constexpr int turn_factor = 5;

class flying_object {
public:

	using context_t = ml5::paint_event::context_t;


	flying_object(wxRealPoint pos, int length) : 
		position{ pos }, length{ length } {}

	virtual void rotate(rotate_direction dir) {
		this->direction += dir == rotate_direction::right ? turn_factor : -turn_factor;
		if (this->direction < 0) {
			this->direction = 360 + this->direction;
		} else if (this->direction >= 360) {
			this->direction = this->direction - 360;
		}
	}

	virtual void draw(context_t& ctx) = 0; 

	virtual void move() = 0;

protected:
	wxRealPoint position;
	int length;
	int direction = 0;
	double speed = 0; // Default flies with zero percent

	/**
	* if an object moves out of sight it loops back
	* at the other side of the window
	**/
	virtual void seemless_move(context_t& ctx) {
		wxSize size = ctx.GetSize();
		if (this->position.x + this->length < 0) {
			this->position.x = size.x - 1;
		}
		if (this->position.y + this->length < 0) {
			this->position.y = size.y - 1;
		}
		if (this->position.x > size.x) {
			this->position.x = this->position.x - size.x;
		}
		if (this->position.y > size.y) {
			this->position.y = this->position.y - size.y;
		}
	}
};