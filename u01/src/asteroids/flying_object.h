#pragma once
#include <ml5/ml5.h>

namespace asteroids {

	enum class rotate_direction {
		right, left
	};

	constexpr int turn_factor = 5;

	class flying_object {
	public:

		using context_t = ml5::paint_event::context_t;

		explicit flying_object(wxRealPoint pos) : position_{ pos } {}

		flying_object(wxRealPoint pos, int const length) :
			position_{ pos }, length_{ length } {}

		virtual ~flying_object() = default;

		virtual void rotate(rotate_direction const dir) {
			this->direction_ += dir == rotate_direction::right ? turn_factor : -turn_factor;
			if (this->direction_ < 0) {
				this->direction_ = 360 + this->direction_;
			} else if (this->direction_ >= 360) {
				this->direction_ = this->direction_ - 360;
			}
		}

		virtual void draw(context_t& ctx) = 0;

		virtual void move() {
			this->position_.x += cos(direction_ * ml5::util::PI / 180) * this->speed_;
			this->position_.y += sin(direction_ * ml5::util::PI / 180) * this->speed_;
		}

	protected:
		wxRealPoint position_;
		int length_ = 0;
		int direction_ = 0;
		double speed_ = 0; // Default flies with zero percent

		/**
		* if an object moves out of sight it loops back
		* at the other side of the window
		**/
		virtual void seamless_move(context_t& ctx) {
			wxSize const size = ctx.GetSize();
			if (this->position_.x + this->length_ < 0) {
				this->position_.x = size.x - 1.0;
			}
			if (this->position_.y + this->length_ < 0) {
				this->position_.y = size.y - 1.0;
			}
			if (this->position_.x > size.x) {
				this->position_.x = this->position_.x - size.x;
			}
			if (this->position_.y > size.y) {
				this->position_.y = this->position_.y - size.y;
			}
		}
	};
}