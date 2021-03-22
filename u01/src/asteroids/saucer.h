#pragma once
#include "flying_object.h"

namespace asteroids {
	constexpr int saucer_length = 30;
	constexpr int saucer_speed_limit = 2;
	constexpr int min_curve_height = 50;
	constexpr int min_curve_width = 200;

	class saucer : public flying_object {
	public:

		explicit saucer(const wxPoint& position)
			: flying_object{position} {
			this->speed_ = (static_cast<double>(rand() % saucer_speed_limit) + 1) * ((rand() % 2 == 0) ? -1 : 1) ;
		}

		saucer(const wxPoint& position,
		                const int max_height,
		                const int max_width)
			: saucer{position} {
			this->y_offset_ = position_.y;
			this->curve_height_ = rand() % (max_height ) ;
			this->curve_width_ = rand() % max_width;
		}

		void draw(context_t& ctx) override {
			this->stay_in_window(ctx);
			ctx.SetPen(*wxYELLOW_PEN);
			ctx.SetBrush(*wxBLACK_BRUSH);
			do_draw(ctx);
		}

		void move() override {
			this->position_.x += this->speed_;
			this->position_.y = this->curve_height_ * sin(to_radiant(static_cast<double>(this->curve_width_) * this->position_.x)) + this->y_offset_;
		}

		int score() {
			return 5;
		}

		[[nodiscard]] wxRealPoint position() const {
			return this->position_;
		}
	
	protected:
		[[nodiscard]] int length() const override {
			return saucer_length;
		}

		[[nodiscard]] std::vector<wxPoint> create_shape() const override {
			return std::vector<wxPoint>{
				wxPoint(0, 0),
				wxPoint(this->length() / 5, this->length() / 3),
				wxPoint((this->length() / 5) * 4, this->length() / 3),
				wxPoint(this->length(), 0),
				wxPoint((this->length() / 5) * 4, -this->length() / 3),
				wxPoint(this->length() / 5, -this->length() / 3),
			};
		}

	private:
		int curve_height_;
		int curve_width_;
		int y_offset_;
	};
}
