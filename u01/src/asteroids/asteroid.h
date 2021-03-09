#pragma once
#include "flying_object.h"

namespace asteroids {

	constexpr int asteroid_min_size = 10;
	constexpr double standard_speed = 2;

	enum class asteroid_size {
		tiny = 1,
		medium = 2,
		big = 3
	};

	class asteroid final : public flying_object {
	public:
		using context_t = ml5::paint_event::context_t;

		explicit asteroid(wxRealPoint const pos) :
			flying_object{ pos } {
			this->size_ = static_cast<asteroid_size>(rand() % 3 + 1);
			this->direction_ = rand() % 360;
			this->speed_ = standard_speed / static_cast<double>(this->size_);
		}

		void draw(context_t& ctx) override {
			this->stay_in_window(ctx);
			ctx.SetBrush(*wxBLACK_BRUSH);
			ctx.SetPen(*wxWHITE_PEN);
			ctx.DrawCircle(this->position_, this->length());
		}

		friend bool operator==(asteroid const& left, asteroid const& right) {
			return left.length() == right.length() && left.position_ == right.position_;
		}

	protected:
		[[nodiscard]] int length() const override	{
			return static_cast<int>(this->size_) * asteroid_min_size;
		}

		[[nodiscard]] std::vector<wxPoint> create_shape() const {
			const std::vector<wxPoint> vec{
				wxPoint(this->length(), 0),
				wxPoint(0, -this->length() / 3),
				wxPoint(0, this->length() / 3)
			};
			return this->transform_points(vec);
		}


	private:
		asteroid_size size_;
	};
}