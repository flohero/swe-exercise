#pragma once
#include "flying_object.h"

constexpr int spaceship_size = 20;
constexpr double acceleration_factor = 0.1;

namespace asteroids {

	class spaceship final : public flying_object {
	public:
		using context_t = ml5::paint_event::context_t;

		spaceship() : 
			spaceship{ wxRealPoint() } {}

		explicit spaceship(wxRealPoint const pos) : flying_object{ pos , spaceship_size } {
		}

		explicit spaceship(int const x, int const y) : flying_object{ wxRealPoint(x, y) , spaceship_size } {
		}
				
		void draw(context_t& ctx) override {
			this->seamless_move(ctx);
			ctx.SetBrush(*wxCYAN_BRUSH);
			ctx.SetPen(*wxCYAN_PEN);
			ctx.DrawRectangle(wxRect{ this->position_, wxSize{spaceship_size, spaceship_size} });
		}

		void accelerate() {
			if (this->speed_ < 1) {
				this->speed_ += acceleration_factor;
			} else {
				this->speed_ = 1;
			}
		}

		void deaccelerate() {
			if (this->speed_ > 0) {
				this->speed_ -= acceleration_factor;
			} else {
				this->speed_ = 0;
			}
		}
	};
}