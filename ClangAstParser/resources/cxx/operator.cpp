#include <string>
#include <iostream>
#include <fstream>
#include <vector>

void test1(std::istream& str, std::string line) {
	auto x = std::getline(str, line).operator bool();
    std::cout << x << std::endl;
}

void test2(std::istream& str, std::string line) {
	while(std::getline(str, line)) {
		auto x = (std::getline(str, line)).operator bool();
	}
}

void test3(std::ifstream str, std::string line) {
   while(str >> line && true) {
   }
}

void testPrePos() {
	
	std::vector<int> vpre;
	for(std::vector<int>::iterator it = vpre.begin(); it != vpre.end(); it++) {
    }
	
	std::vector<int> vpos;
	for(std::vector<int>::iterator it2 = vpos.begin(); it2 != vpos.end(); ++it2) {
    }
}

int main() {
   
   return 0;
}