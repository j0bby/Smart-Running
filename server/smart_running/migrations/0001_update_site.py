# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib.sites.models import Site
from django.db import models, migrations


def create_site(apps, schema_editor):
    s = Site.objects.create(domain='127.0.0.1:8000', name='127.0.0.1')
    s.save()


class Migration(migrations.Migration):
    dependencies = [
    ]

    operations = [
        migrations.RunPython(create_site)
    ]
