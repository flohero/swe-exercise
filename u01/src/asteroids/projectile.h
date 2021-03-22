#pragma once
#include "flying_object.h"

namespace asteroids {
	constexpr int projectile_speed = 10;
	constexpr int projectile_size = 5;


	class projectile : public flying_object {
	public:
		projectile(wxRealPoint const& position,
			int const direction, bool is_enemy = false)
			: flying_object{position} {
			this->direction_ = direction;
			this->speed_ = projectile_speed;
			this->is_enemy_ = is_enemy;
		}

		void draw(context_t& ctx) override {
			if(this->is_enemy_) {
				ctx.SetBrush(*wxRED_BRUSH);
				ctx.SetPen(*wxRED_PEN);
			} else {
				ctx.SetBrush(*wxGREEN_BRUSH);
				ctx.SetPen(*wxGREEN_PEN);
			}
			ctx.DrawRectangle(this->position_,
			                  wxSize{projectile_size, projectile_size});
		}

		/**
		 * Check if projectile is still in window
		 */
		[[nodiscard]] bool is_in_window(const int width, const int height) const {
			return !(this->position_.x < 0 || this->position_.x > width || this->position_.y < 0 || this->position_.y > height);
		}

	protected:
		[[nodiscard]] int length() const override {
			return projectile_size;
		}

		[[nodiscard]] std::vector<wxPoint> create_shape() const override {
			return std::vector<wxPoint>{
				wxPoint{0, 0},
				wxPoint{this->length(), 0},
				wxPoint{this->length(), this->length()},
				wxPoint{0, this->length()},
			};
		}

	private:
		bool is_enemy_;
	};
}
