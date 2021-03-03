#include <ml5/gui/application.h>
#include "asteroids_window.h"
#pragma once

namespace asteroids {

	class  asteroids_app : public ml5::application {
	protected:
		std::unique_ptr <ml5::window> make_window() const override {
			return std::make_unique<asteroids_window>();
		}
	};
}
