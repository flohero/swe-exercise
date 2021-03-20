#pragma once
#include <ml5/ml5.h>
#include "spaceship.h"
#include "asteroid.h"

namespace asteroids {
	constexpr int tick_interval = 10;
	constexpr int window_width = 800;
	constexpr int window_height = 600;
	constexpr int asteroid_limit = 20;

	class asteroids_window final : public ml5::window {
	public:
		using context_t = ml5::paint_event::context_t;

		asteroids_window() {
			set_prop_allow_resize(false);
			set_prop_initial_size({window_width, window_height});
		}

		void on_init() override {
			set_prop_background_brush(*wxBLACK_BRUSH);
			start_timer(std::chrono::milliseconds{tick_interval});
			this->ship_ = spaceship{this->get_width() / 2, this->get_height() / 2};
		}

		void on_paint(ml5::paint_event const& event) override {
			set_status_text("Score: " + std::to_string(score) + 
				"; Speed: " + std::to_string(ship_.speed()));

			auto& ctx = event.get_context();
			spawn_asteroid(ctx);

			auto s = ctx.GetSize();
			ship_.draw(ctx);
			for (auto& asteroid : this->asteroids_) {
				asteroid.draw(ctx);
			}
		}

		void on_timer(ml5::timer_event const& event) override {
			ship_.move();
			for (auto& asteroid : this->asteroids_) {
				asteroid.move();
				if (this->ship_.has_collision(asteroid)) {
					std::cout << "Collision" << std::endl;
				}
			}

			/* Ugly hack since there is no key up or down event.*/
			if (not_accelerated_count_ > 10000) {
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

	private:
		spaceship ship_;
		std::vector<asteroid> asteroids_;
		int not_accelerated_count_ = 0;
		int score = 0;

		void spawn_asteroid(const context_t& ctx) {
			if (rand() % 100 != 0
				|| this->asteroids_.size() > asteroid_limit) {
				return;
			}
			std::cout << "Spawning Asteroid" << std::endl;
			auto size = ctx.GetSize();
			auto const x = rand() % size.GetWidth()
				+ static_cast<double>(rand() % 2 == 0 ? size.GetWidth() : -size.GetWidth());
			auto const y = rand() % size.GetHeight()
				+ static_cast<double>(rand() % 2 == 0 ? size.GetHeight() : -size.GetHeight());
			const asteroid ast{wxRealPoint{x, y}};
			this->asteroids_.emplace_back(ast);
		}

		void game_over() {
			//TODO
		}
	};
}
