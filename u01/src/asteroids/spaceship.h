#pragma once
#include "flying_object.h"

constexpr int spaceship_size = 30;
constexpr double acceleration_factor = 0.1;

namespace asteroids {

	class spaceship final : public flying_object {
	public:
		using context_t = ml5::paint_event::context_t;

		spaceship() : 
			spaceship{ wxRealPoint() } {}

		explicit spaceship(wxRealPoint const pos) : flying_object{ pos} {
		}

		spaceship(int const x, int const y) : flying_object{ wxRealPoint(x, y) } {
		}
				
		void draw(context_t& ctx) override {
			this->stay_in_window(ctx);
			ctx.SetBrush(*wxCYAN_BRUSH);
			ctx.SetPen(*wxCYAN_PEN);
			auto points_vec = this->create_shape();
			ctx.DrawPolygon(3, &points_vec[0], (this->position_.x), (this->position_.y), wxWINDING_RULE);
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

	protected:

		[[nodiscard]] int length() const override {
			return spaceship_size;
		}

		[[nodiscard]] std::vector<wxPoint> create_shape() const	{
			const std::vector<wxPoint> vec{
				wxPoint(this->length(), 0),
				wxPoint(0, -this->length() / 3),
				wxPoint(0, this->length() / 3)
			};
			return this->transform_points(vec);
		}
	};
}