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
			this->position_.x += cos(to_radiant()) * this->speed_;
			this->position_.y += sin(to_radiant()) * this->speed_;
		}


	protected:
		wxRealPoint position_;
		int direction_ = 0; // The angle of the object in degree
		double speed_ = 0; // Default flies with zero percent

		/**
		 * Convert the direction to a radiant from degree
		 */
		double to_radiant() const	{
			return this->direction_ * ml5::util::PI / 180;
		}

		/*
		 * Rotate points according to the transformation matrix
		 */

		std::vector<wxPoint> transform_points(std::vector<wxPoint> points) const {
			for (auto &point: points) {
				auto const old = point;
				point.x = old.x * cos(to_radiant()) - old.y * sin(to_radiant());
				point.y = old.x * sin(to_radiant()) + old.y * cos(to_radiant());
			}
			return points;
		}

		/**
		* if an object moves out of sight it loops back
		* at the other side of the window
		**/
		virtual void stay_in_window(context_t& ctx) {
			auto const size = ctx.GetSize();
			if (this->position_.x + this->length() < 0) {
				this->position_.x = size.x - 1.0;
			}
			if (this->position_.y + this->length() < 0) {
				this->position_.y = size.y - 1.0;
			}
			if (this->position_.x > size.x) {
				this->position_.x = this->position_.x - size.x;
			}
			if (this->position_.y > size.y) {
				this->position_.y = this->position_.y - size.y;
			}
		}
		
		virtual int length() const = 0;

	};
}