//
// Created by florian on 27.03.21.
//

#include <cstdlib>
#include "simulator.h"
#include "scenario/start_event.h"

void test_produce_enough_products() {
  des::simulator{
      std::make_shared<des::start_event>(50, time(nullptr) * 2)
  }.run();
}

void test_produce_enough_products_with_step() {
  des::simulator sim{
      std::make_shared<des::start_event>(50, time(nullptr) * 2)
  };
  while(!sim.step()) {
    sleep(1);
  }
}

void test_time_terminates() {
  const time_t in_a_min = time(nullptr) + 60;
  des::simulator{
      std::make_shared<des::start_event>(1000000, in_a_min)
  }.run();
}

void test_time_terminates_with_step() {
  const time_t in_a_min = time(nullptr) + 60;
  des::simulator sim{
      std::make_shared<des::start_event>(1000000, in_a_min)
  };
  while(!sim.step()) {
    sleep(1);
  }
}

int main() {
  srand(time(nullptr));
  test_time_terminates_with_step();
  return EXIT_SUCCESS;
}