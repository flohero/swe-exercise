#include "./asteroids_app.h"


int main() {
	srand(time(NULL));
	asteroids::asteroids_app{}.run();
}