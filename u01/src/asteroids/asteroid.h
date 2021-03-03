#pragma once
#include "flying_object.h"

namespace asteroids {

	constexpr int asteroid_min_size = 10;

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
			this->length_ = static_cast<int>(this->size_) * asteroid_min_size;
		}

		void draw(context_t& ctx) override {
			this->seamless_move(ctx);
			ctx.SetBrush(*wxWHITE_BRUSH);
			ctx.SetPen(*wxWHITE_PEN);
			ctx.DrawRectangle(wxRect{ this->position_, wxSize{this->length_, this->length_} });
		}

		friend bool operator==(asteroid const& left, asteroid const& right) {
			return left.length_ == right.length_ && left.position_ == right.position_;
		}

	private:
		asteroid_size size_;
	};
}