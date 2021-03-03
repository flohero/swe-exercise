#include "./asteroids_app.h"


int main() {
	srand(time(nullptr));
	asteroids::asteroids_app{}.run();
}