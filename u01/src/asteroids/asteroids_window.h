#pragma once
#include <ml5/ml5.h>
#include "spaceship.h"
#include "asteroid.h"
#include "projectile.h"
#include "saucer.h"

namespace asteroids {
	constexpr int tick_interval = 10;
	constexpr int window_width = 800;
	constexpr int window_height = 600;
	constexpr int asteroid_limit = 20;
	constexpr int saucer_limit = 4;
	constexpr int ticks_between_shots = 20;
	constexpr int asteroid_spawn_chance = 200;
	constexpr int saucer_spawn_chance = 500;
	constexpr int enemy_projectile_spawn_chance = 300;

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
			spawn_saucer();
			spawn_enemy_projectile();

			auto s = ctx.GetSize();
			ship_.draw(ctx);
			for (auto& asteroid : this->asteroids_) {
				asteroid.draw(ctx);
			}

			for (auto& projectile : this->projectiles_) {
				projectile.draw(ctx);
			}
			for (auto& sauce : this->saucers_) {
				sauce.draw(ctx);
			}
			for(auto& projectile: this->enemy_projectiles_) {
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

				if (had_collision) {
					auto split_asts = asteroid->split();
					this->asteroids_.erase(asteroid);
					this->asteroids_.insert(this->asteroids_.end(), split_asts.begin(), split_asts.end());
					continue;
				}

				auto sauce = this->saucers_.begin();
				while (!had_collision && sauce < this->saucers_.end()) {
					if (proj.has_collision(*sauce)) {
						this->score_ += sauce->score();
						had_collision = true;
					} else {
						++sauce;
					}
				}

				if (!had_collision) {
					if (proj.is_in_window(this->get_width(), this->get_height())) {
						new_projectiles.push_back(proj);
					}
				} else {
					this->saucers_.erase(sauce);
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
			for (auto& saucer : this->saucers_) {
				if (this->ship_.has_collision(saucer)) {
					game_over();
				}
				saucer.move();
			}

			for(auto& projectile: this->enemy_projectiles_) {
				if (this->ship_.has_collision(projectile)) {
					game_over();
				}
				projectile.move();
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
			const auto& item{event.get_item()};

			if (item == "Restart") {
				this->ship_ = spaceship{this->get_width() / 2, this->get_height() / 2};
				this->asteroids_.clear();
				this->projectiles_.clear();
				this->enemy_projectiles_.clear();
				this->saucers_.clear();
				this->score_ = 0;
				this->not_accelerated_count_ = 0;
				this->count_down_for_projectiles_ = 0;
				this->game_over_ = false;
				start_timer(std::chrono::milliseconds{tick_interval});
			}
		}

	private:
		spaceship ship_;
		std::vector<asteroid> asteroids_;
		std::vector<projectile> projectiles_;
		std::vector<projectile> enemy_projectiles_;
		std::vector<saucer> saucers_;
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
			double x = 0;
			double y = 0;
			switch (rand() % 4) {
			case 2: {
				// Spawn bottom
				y = size.GetHeight();
			}
			case 0: {
				// Spawn top
				x = rand() % size.GetWidth();
				break;
			}
			case 1: {
				// Spawn right
				x = size.GetWidth();
			}
			case 3: {
				// Spawn left
				y = rand() % size.GetHeight();
				break;
			}
			default: break;
			}
			const asteroid ast{wxRealPoint{x, y}};
			this->asteroids_.push_back(ast);
		}

		void spawn_saucer() {
			if (rand() % saucer_spawn_chance != 0
				|| this->saucers_.size() > saucer_limit) {
				return;
			}
			this->saucers_.push_back(saucer{wxPoint{0, this->get_height() / 2}, this->get_height() / 2, 3});
		}

		void spawn_enemy_projectile() {
			if (this->saucers_.empty() || rand() % enemy_projectile_spawn_chance != 0) {
				return;
			}
			auto const start = this->saucers_
			                 .at(rand() % this->saucers_.size())
			                 .position();
			auto const vec = this->ship_.position() - start;
			int const direction = atan(vec.y / vec.x) * 180 / ml5::util::PI;
			this->enemy_projectiles_.push_back(projectile{
				start,
				direction
			});
		}

		void game_over() {
			this->game_over_ = true;
			this->refresh();
			this->stop_timer();
		}
	};
}
