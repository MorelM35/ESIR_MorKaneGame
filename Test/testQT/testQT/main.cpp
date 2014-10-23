#include <QtGui/qapplication.h>
#include <QtGui/qpushButton.h>

int main(int argc, char *argv[])
{
	QApplication app(argc, argv);

	QPushButton bouton("Bonjour les Z�ros !");
	bouton.show();

	QObject::connect(&bouton, SIGNAL(clicked()), &app, SLOT(quit()));

	return app.exec();
}