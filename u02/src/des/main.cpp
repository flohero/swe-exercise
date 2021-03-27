//
// Created by florian on 27.03.21.
//

#include <cstdlib>
#include "events/event.h"
#include "simulator.h"
#include "events/basic_event.h"

int main() {
  des::simulator{
    std::make_shared<des::basic_event>("1"),
    std::make_shared<des::basic_event>("2"),
    std::make_shared<des::basic_event>("3"),
    std::make_shared<des::basic_event>("4"),
  }.run();
  return EXIT_SUCCESS;
}