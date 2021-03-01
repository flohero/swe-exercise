#include <ml5/ml5.h>
#include "spaceship.h"
#pragma once

constexpr int TICK_INTERVAL = 10;

class asteroids_window: public ml5::window {
public:
	using context_t = ml5::paint_event::context_t;

	void on_init() override {
		set_prop_background_brush(*wxBLACK_BRUSH);
		start_timer(std::chrono::milliseconds{ TICK_INTERVAL });
		this->ship = spaceship{this->get_width() / 2, this->get_height() / 2};
	}

	void on_paint(ml5::paint_event const& event) override {
		context_t &ctx = event.get_context();
		auto s = ctx.GetSize();
		ship.draw(ctx);
	}

	void on_timer(ml5::timer_event const& event) override {
		ship.move();

		/* Ugly hack since there is no key up or down event.*/
		if (not_accelerated_count > 1000) {
			ship.deaccelerate();
		} else {
			not_accelerated_count += 1;
		}
		refresh();
	}

	void on_key(ml5::key_event const& event) override {
		if (event.get_key_code() == 'w') {
			ship.accelerate();
			not_accelerated_count = 0;
		} else if(event.get_key_code() == 'a') {
			ship.rotate(rotate_direction::left);
		}else if(event.get_key_code() == 'd') {
			ship.rotate(rotate_direction::right);
		}
	}

private:
	spaceship ship;
	int not_accelerated_count = 0;
};

