#pragma once
#include "flying_object.h"

namespace asteroids {

	constexpr int asteroid_min_size = 10;
	constexpr int crack_width = 30;
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
			flying_object{pos} {
			this->size_ = static_cast<asteroid_size>(rand() % 3 + 1);
			this->direction_ = rand() % full_degree;
			this->speed_ = standard_speed / static_cast<double>(this->size_);
			this->crack_start_ = rand() % full_degree;
			this->crack_end = crack_start_ + crack_width;
		}

		void draw(context_t& ctx) override {
			this->stay_in_window(ctx);
			ctx.SetBrush(*wxBLACK_BRUSH);
			ctx.SetPen(*wxWHITE_PEN);
			do_draw(ctx);
		}

		friend bool operator==(asteroid const& left, asteroid const& right) {
			return left.length() == right.length() && left.position_ == right.position_;
		}

		

	protected:
		[[nodiscard]] int length() const override {
			return static_cast<int>(this->size_) * asteroid_min_size;
		}

		[[nodiscard]] std::vector<wxPoint> create_shape() const override {
			std::vector<wxPoint> vec;
			for (auto i = 0; i < full_degree  / 2; i++) {
				wxPoint p;
				auto const point = i * 2;
				if (point > crack_start_ && point < crack_end) {
					p.x = cos(to_radiant(point)) * this->length() / 4;
					p.y = sin(to_radiant(point)) * this->length() / 4;
				} else {
					p.x = cos(to_radiant(point)) * this->length();
					p.y = sin(to_radiant(point)) * this->length();
				}

				vec.push_back(p);
			}
			return this->transform_points(vec);
		}


	private:
		asteroid_size size_;
		int crack_start_;
		int crack_end;;
	};
}
