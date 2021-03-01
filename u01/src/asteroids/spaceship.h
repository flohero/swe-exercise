#include "flying_object.h"
#include <ml5/utility/constants.h>
#pragma once

constexpr int SPACESHIP_SIZE = 20;
constexpr double acceleration_factor = 0.1;


class spaceship : public flying_object {
public:
	using context_t = ml5::paint_event::context_t;

	spaceship() : spaceship{ wxRealPoint()} {}

	explicit spaceship(wxRealPoint pos) : flying_object{ pos , SPACESHIP_SIZE} {}

	explicit spaceship(int x, int y) : flying_object{ wxRealPoint(x, y) , SPACESHIP_SIZE } {}

	void draw(context_t &ctx) override {
		this->seemless_move(ctx);
		ctx.SetBrush(*wxCYAN_BRUSH);
		ctx.SetPen(*wxCYAN_PEN);
		ctx.DrawRectangle(wxRect{ this->position, wxSize{SPACESHIP_SIZE, SPACESHIP_SIZE} });
	}

	void move() final {
		this->position.x += cos(direction * ml5::util::PI / 180) * this->speed;
		this->position.y += sin(direction * ml5::util::PI / 180) * this->speed;
	}

	void accelerate() {
		if (this->speed < 1) {
			this->speed += acceleration_factor;
		} else {
			this->speed = 1;
		}
	}

	void deaccelerate() {
		if (this->speed > 0) {
			this->speed -= acceleration_factor;
		} else {
			this->speed = 0;
		}
	}
};