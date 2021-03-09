#pragma once
#include <ml5/ml5.h>
#include "spaceship.h"
#include "asteroid.h"

namespace asteroids {
	constexpr int TICK_INTERVAL = 10;

	class asteroids_window final : public ml5::window {
	public:
		using context_t = ml5::paint_event::context_t;

		void on_init() override {
			set_prop_background_brush(*wxBLACK_BRUSH);
			start_timer(std::chrono::milliseconds{ TICK_INTERVAL });
			this->ship_ = spaceship{ this->get_width() / 2, this->get_height() / 2 };
			asteroid ast{ wxRealPoint { 0.0, 0.0 } };
			this->asteroids_.push_back(ast);
		}

		void on_paint(ml5::paint_event const& event) override {
			context_t& ctx = event.get_context();
			auto s = ctx.GetSize();
			ship_.draw(ctx);
			for (auto &asteroid : this->asteroids_) {
				asteroid.draw(ctx);
			}
		}

		void on_timer(ml5::timer_event const& event) override {
			ship_.move();
			for (auto &asteroid : this->asteroids_) {
				asteroid.move();
			}

			/* Ugly hack since there is no key up or down event.*/
			if (not_accelerated_count_ > 1000) {
				ship_.deaccelerate();
			} else {
				not_accelerated_count_ += 1;
			}
			refresh();
		}

		void on_key(ml5::key_event const& event) override {
			if (event.get_key_code() == 'w') {
				ship_.accelerate();
				not_accelerated_count_ = 0;
			} else if (event.get_key_code() == 'a') {
				ship_.rotate(rotate_direction::left);
			} else if (event.get_key_code() == 'd') {
				ship_.rotate(rotate_direction::right);
			}
		}

		void spawn_asteroid() {

		}

	private:
		spaceship ship_;
		std::vector<asteroid> asteroids_;
		int not_accelerated_count_ = 0;
	};
}

