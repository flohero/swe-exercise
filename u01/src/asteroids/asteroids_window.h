#pragma once
#include <ml5/ml5.h>
#include "spaceship.h"
#include "asteroid.h"
#include "projectile.h"

namespace asteroids {
	constexpr int tick_interval = 10;
	constexpr int window_width = 800;
	constexpr int window_height = 600;
	constexpr int asteroid_limit = 20;
	constexpr int ticks_between_shots = 20;
	constexpr int asteroid_spawn_chance = 200;

	class asteroids_window final : public ml5::window {
	public:
		using context_t = ml5::paint_event::context_t;

		asteroids_window() : window{"A really cool Game! :("} {
			set_prop_allow_resize(false);
			set_prop_initial_size({window_width, window_height});
		}

		void on_init() override {
			set_prop_background_brush(*wxBLACK_BRUSH);
			start_timer(std::chrono::milliseconds{tick_interval});
			this->ship_ = spaceship{this->get_width() / 2, this->get_height() / 2};

			add_menu("Game", {
				{"Restart", "Restart the game"},
			});
		}

		void on_paint(ml5::paint_event const& event) override {
			set_status_text("Score: " + std::to_string(score_) +
				"; Speed: " + std::to_string(ship_.speed()));

			auto& ctx = event.get_context();
			if (game_over_) {
				ctx.SetPen(*wxRED_PEN);
				ctx.SetBrush(*wxRED_BRUSH);
				ctx.SetTextForeground(*wxRED);
				const wxFont font(50, wxFONTFAMILY_TELETYPE, wxFONTSTYLE_NORMAL, wxFONTWEIGHT_BOLD);
				ctx.SetFont(font);
				ctx.DrawText("GAME OVER", 225, this->get_height() / 4);
				ctx.DrawText("Your Score:", 200, (this->get_height() / 2));
				ctx.DrawText(std::to_string(this->score_), 225, 400);
				return;
			}

			spawn_asteroid(ctx);

			auto s = ctx.GetSize();
			ship_.draw(ctx);
			for (auto& asteroid : this->asteroids_) {
				asteroid.draw(ctx);
			}

			for (auto& projectile : this->projectiles_) {
				projectile.draw(ctx);
			}
		}

		void on_timer(ml5::timer_event const& event) override {
			std::vector<projectile> new_projectiles;
			for (const auto& proj : this->projectiles_) {
				bool had_collision = false;
				auto asteroid = this->asteroids_.begin();
				while (!had_collision && asteroid < this->asteroids_.end()) {
					if (proj.has_collision(*asteroid)) {
						this->score_ += asteroid->score();
						had_collision = true;
					} else {
						++asteroid;
					}
				}

				if (!had_collision) {
					if (proj.is_in_window(this->get_width(), this->get_height())) {
						new_projectiles.push_back(proj);
					}
				} else {
					auto split_asts = asteroid->split();
					this->asteroids_.erase(asteroid);
					this->asteroids_.insert(this->asteroids_.end(), split_asts.begin(), split_asts.end());
				}
			}
			this->projectiles_ = new_projectiles;
			ship_.move();
			for (auto& asteroid : this->asteroids_) {
				asteroid.move();
				if (this->ship_.has_collision(asteroid)) {
					game_over();
				}
			}

			for (auto& projectile : this->projectiles_) {
				projectile.move();
			}

			count_down_for_projectiles_--;

			/* Ugly hack since there is no key up or down event.*/
			if (not_accelerated_count_ > 10000) {
				ship_.deaccelerate();
			} else {
				not_accelerated_count_ += 1;
			}
			refresh();
		}

		void on_key(ml5::key_event const& event) override {
			switch (event.get_key_code()) {
			case 'w': {
				ship_.accelerate();
				not_accelerated_count_ = 0;
				break;
			}
			case 'a': {
				ship_.rotate(rotate_direction::left);
				break;
			}
			case 'd': {
				ship_.rotate(rotate_direction::right);
				break;
			}
			case WXK_SPACE: {
				if (count_down_for_projectiles_ <= 0) {
					count_down_for_projectiles_ = ticks_between_shots;
					projectile pro{ship_.position(), ship_.direction()};
					projectiles_.emplace_back(pro);
				}
				break;
			}
			default: break;
			}
		}

		void on_menu(ml5::menu_event const& event) override {
			const auto item{ event.get_item() };

			if (item == "Restart") {
				this->ship_ = spaceship{ this->get_width() / 2, this->get_height() / 2 };
				this->asteroids_.clear();
				this->projectiles_.clear();
				this->score_ = 0;
				this->not_accelerated_count_ = 0;
				this->count_down_for_projectiles_ = 0;
				this->game_over_ = false;
				this->refresh();
			}
		}

	private:
		spaceship ship_;
		std::vector<asteroid> asteroids_;
		std::vector<projectile> projectiles_;
		int not_accelerated_count_ = 0;
		int count_down_for_projectiles_ = 0;
		int score_ = 0;
		bool game_over_ = false;

		void spawn_asteroid(const context_t& ctx) {
			if (rand() % asteroid_spawn_chance != 0
				|| this->asteroids_.size() > asteroid_limit) {
				return;
			}
			auto const size = ctx.GetSize();
			auto const x = rand() % size.GetWidth()
				+ static_cast<double>(rand() % 2 == 0 ? size.GetWidth() : -size.GetWidth());
			auto const y = rand() % size.GetHeight()
				+ static_cast<double>(rand() % 2 == 0 ? size.GetHeight() : -size.GetHeight());
			const asteroid ast{wxRealPoint{x, y}};
			this->asteroids_.emplace_back(ast);
		}

		void game_over() {
			//TODO
			this->game_over_ = true;
			std::cout << "Game Over!" << std::endl;
		}
	};
}
